
import React, { useState } from 'react';
import { MainLayout } from '@/components/layout/MainLayout';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import { Separator } from '@/components/ui/separator';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { Label } from '@/components/ui/label';
import { Tabs, TabsList, TabsTrigger, TabsContent } from '@/components/ui/tabs';
import { useAuth } from '@/contexts/AuthContext';
import { toast } from 'sonner';
import { updateProfile, updateEmail, updatePassword, User } from 'firebase/auth';
import { Bell, Camera, CheckCircle, Key, User as UserIcon, Mail, Shield } from 'lucide-react';

const ProfilePage = () => {
  const { currentUser } = useAuth();
  const [isLoading, setIsLoading] = useState(false);
  
  const [profileData, setProfileData] = useState({
    displayName: currentUser?.displayName || '',
    email: currentUser?.email || '',
    bio: '',
    currentPassword: '',
    newPassword: '',
    confirmPassword: '',
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setProfileData(prev => ({ ...prev, [name]: value }));
  };

  const handleProfileUpdate = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!currentUser) return;
    
    setIsLoading(true);
    try {
      await updateProfile(currentUser, {
        displayName: profileData.displayName,
      });
      
      toast.success("Profil mis à jour avec succès");
    } catch (error) {
      console.error("Error updating profile:", error);
      toast.error("Erreur lors de la mise à jour du profil");
    } finally {
      setIsLoading(false);
    }
  };

  const handleEmailUpdate = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!currentUser) return;
    
    setIsLoading(true);
    try {
      await updateEmail(currentUser, profileData.email);
      toast.success("Email mis à jour avec succès");
    } catch (error) {
      console.error("Error updating email:", error);
      toast.error("Erreur lors de la mise à jour de l'email");
    } finally {
      setIsLoading(false);
    }
  };

  const handlePasswordUpdate = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!currentUser) return;
    
    if (profileData.newPassword !== profileData.confirmPassword) {
      toast.error("Les mots de passe ne correspondent pas");
      return;
    }
    
    setIsLoading(true);
    try {
      await updatePassword(currentUser, profileData.newPassword);
      setProfileData(prev => ({
        ...prev,
        currentPassword: '',
        newPassword: '',
        confirmPassword: '',
      }));
      toast.success("Mot de passe mis à jour avec succès");
    } catch (error) {
      console.error("Error updating password:", error);
      toast.error("Erreur lors de la mise à jour du mot de passe");
    } finally {
      setIsLoading(false);
    }
  };

  const getInitials = (name: string | null) => {
    if (!name) return 'U';
    return name
      .split(' ')
      .map(part => part.charAt(0))
      .join('')
      .toUpperCase();
  };

  return (
    <MainLayout>
      <div className="space-y-6">
        <div>
          <h1 className="text-3xl font-bold mb-2">Profil</h1>
          <p className="text-gray-500 dark:text-gray-400">
            Gérez vos informations personnelles et vos paramètres
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
          <Card className="md:col-span-1">
            <CardContent className="pt-6 flex flex-col items-center">
              <div className="relative mb-4">
                <Avatar className="w-24 h-24">
                  <AvatarImage src={currentUser?.photoURL || undefined} alt={currentUser?.displayName || 'Utilisateur'} />
                  <AvatarFallback className="text-lg">{getInitials(currentUser?.displayName)}</AvatarFallback>
                </Avatar>
                <Button size="icon" variant="secondary" className="absolute bottom-0 right-0 rounded-full w-8 h-8">
                  <Camera className="h-4 w-4" />
                </Button>
              </div>
              
              <h2 className="text-xl font-semibold mb-1">{currentUser?.displayName || 'Utilisateur'}</h2>
              <p className="text-sm text-gray-500 dark:text-gray-400 mb-4">{currentUser?.email}</p>
              
              <Separator className="mb-6" />
              
              <div className="w-full space-y-2">
                <div className="flex items-center p-2 rounded-md hover:bg-gray-100 dark:hover:bg-gray-800">
                  <UserIcon className="mr-2 h-5 w-5 text-gray-500" />
                  <span>Profil</span>
                </div>
                <div className="flex items-center p-2 rounded-md hover:bg-gray-100 dark:hover:bg-gray-800">
                  <Shield className="mr-2 h-5 w-5 text-gray-500" />
                  <span>Sécurité</span>
                </div>
                <div className="flex items-center p-2 rounded-md hover:bg-gray-100 dark:hover:bg-gray-800">
                  <Bell className="mr-2 h-5 w-5 text-gray-500" />
                  <span>Notifications</span>
                </div>
              </div>
              
              <Separator className="my-6" />
              
              <div className="w-full">
                <p className="text-sm text-gray-500 dark:text-gray-400 mb-2">Membre depuis</p>
                <p className="text-sm">
                  {currentUser?.metadata.creationTime 
                    ? new Date(currentUser.metadata.creationTime).toLocaleDateString('fr-FR')
                    : 'Date inconnue'}
                </p>
              </div>
            </CardContent>
          </Card>
          
          <div className="md:col-span-3">
            <Tabs defaultValue="profile">
              <TabsList className="grid w-full grid-cols-3">
                <TabsTrigger value="profile" className="flex items-center justify-center">
                  <UserIcon className="mr-2 h-4 w-4" />
                  Profil
                </TabsTrigger>
                <TabsTrigger value="email" className="flex items-center justify-center">
                  <Mail className="mr-2 h-4 w-4" />
                  Email
                </TabsTrigger>
                <TabsTrigger value="password" className="flex items-center justify-center">
                  <Key className="mr-2 h-4 w-4" />
                  Mot de passe
                </TabsTrigger>
              </TabsList>
              
              <TabsContent value="profile">
                <Card>
                  <form onSubmit={handleProfileUpdate}>
                    <CardHeader>
                      <CardTitle>Informations personnelles</CardTitle>
                      <CardDescription>
                        Mettez à jour vos informations personnelles
                      </CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-4">
                      <div className="space-y-2">
                        <Label htmlFor="displayName">Nom complet</Label>
                        <Input
                          id="displayName"
                          name="displayName"
                          value={profileData.displayName}
                          onChange={handleChange}
                          placeholder="Votre nom"
                        />
                      </div>
                      
                      <div className="space-y-2">
                        <Label htmlFor="bio">Bio</Label>
                        <Textarea
                          id="bio"
                          name="bio"
                          value={profileData.bio}
                          onChange={handleChange}
                          placeholder="Parlez-nous de vous"
                          rows={4}
                        />
                      </div>
                    </CardContent>
                    <CardFooter className="flex justify-between">
                      <Button variant="outline">Annuler</Button>
                      <Button type="submit" disabled={isLoading}>
                        {isLoading ? 'Enregistrement...' : 'Enregistrer les modifications'}
                      </Button>
                    </CardFooter>
                  </form>
                </Card>
              </TabsContent>
              
              <TabsContent value="email">
                <Card>
                  <form onSubmit={handleEmailUpdate}>
                    <CardHeader>
                      <CardTitle>Changer l'adresse email</CardTitle>
                      <CardDescription>
                        Mettez à jour votre adresse email
                      </CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-4">
                      <div className="space-y-2">
                        <Label htmlFor="email">Adresse email</Label>
                        <Input
                          id="email"
                          name="email"
                          type="email"
                          value={profileData.email}
                          onChange={handleChange}
                          placeholder="votre@email.com"
                        />
                      </div>
                      
                      <div className="bg-yellow-50 dark:bg-yellow-950 p-4 rounded-md">
                        <p className="text-sm text-yellow-800 dark:text-yellow-300">
                          <strong>Note:</strong> La modification de votre adresse email nécessitera une vérification.
                        </p>
                      </div>
                    </CardContent>
                    <CardFooter className="flex justify-between">
                      <Button variant="outline">Annuler</Button>
                      <Button type="submit" disabled={isLoading}>
                        {isLoading ? 'Mise à jour...' : 'Mettre à jour l\'email'}
                      </Button>
                    </CardFooter>
                  </form>
                </Card>
              </TabsContent>
              
              <TabsContent value="password">
                <Card>
                  <form onSubmit={handlePasswordUpdate}>
                    <CardHeader>
                      <CardTitle>Changer le mot de passe</CardTitle>
                      <CardDescription>
                        Mettez à jour votre mot de passe pour sécuriser votre compte
                      </CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-4">
                      <div className="space-y-2">
                        <Label htmlFor="currentPassword">Mot de passe actuel</Label>
                        <Input
                          id="currentPassword"
                          name="currentPassword"
                          type="password"
                          value={profileData.currentPassword}
                          onChange={handleChange}
                          placeholder="••••••••"
                        />
                      </div>
                      
                      <div className="space-y-2">
                        <Label htmlFor="newPassword">Nouveau mot de passe</Label>
                        <Input
                          id="newPassword"
                          name="newPassword"
                          type="password"
                          value={profileData.newPassword}
                          onChange={handleChange}
                          placeholder="••••••••"
                        />
                      </div>
                      
                      <div className="space-y-2">
                        <Label htmlFor="confirmPassword">Confirmer le mot de passe</Label>
                        <Input
                          id="confirmPassword"
                          name="confirmPassword"
                          type="password"
                          value={profileData.confirmPassword}
                          onChange={handleChange}
                          placeholder="••••••••"
                        />
                      </div>
                      
                      <div className="bg-blue-50 dark:bg-blue-950 p-4 rounded-md">
                        <p className="text-sm text-blue-800 dark:text-blue-300">
                          <CheckCircle className="inline-block h-4 w-4 mr-1" />
                          Utilisez au moins 8 caractères, incluant des lettres majuscules, minuscules, chiffres et symboles.
                        </p>
                      </div>
                    </CardContent>
                    <CardFooter className="flex justify-between">
                      <Button variant="outline">Annuler</Button>
                      <Button type="submit" disabled={isLoading}>
                        {isLoading ? 'Mise à jour...' : 'Mettre à jour le mot de passe'}
                      </Button>
                    </CardFooter>
                  </form>
                </Card>
              </TabsContent>
            </Tabs>
          </div>
        </div>
      </div>
    </MainLayout>
  );
};

export default ProfilePage;
